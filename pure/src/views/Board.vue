<template>
  <div class="board-page">
    <div class="board-hero">
      <div class="container">
        <h1 class="board-hero-title">고객 문의</h1>
        <p class="board-hero-desc">궁금한 점을 남겨주시면 빠르게 답변드리겠습니다.</p>
      </div>
    </div>

    <div class="container board-container">
      <div class="board-actions">
        <button v-if="isLoggedIn" class="btn-write" @click="openWriteModal">문의 작성</button>
        <RouterLink v-else to="/login" class="btn-write">로그인 후 문의하기</RouterLink>
      </div>

      <div v-if="loading" class="loading-box"><div class="spinner"></div></div>

      <div v-else-if="questions.length === 0" class="empty-box">
        <p>등록된 문의가 없습니다.</p>
      </div>

      <div v-else class="board-table-wrap">
        <table class="board-table">
          <thead>
            <tr>
              <th class="col-num">번호</th>
              <th class="col-title">제목</th>
              <th class="col-author">작성자</th>
              <th class="col-status">상태</th>
              <th class="col-date">작성일</th>
            </tr>
          </thead>
          <tbody>
            <template v-for="q in questions" :key="q.id">
              <tr class="board-row" :class="{ 'board-row--secret': q.isSecret }" @click="toggleRow(q.id)">
                <td class="col-num">{{ q.id }}</td>
                <td class="col-title">
                  <span v-if="q.isSecret" class="secret-icon">🔒 </span>
                  {{ getTitle(q) }}
                </td>
                <td class="col-author">{{ q.memberName }}</td>
                <td class="col-status">
                  <span class="qna-badge" :class="q.isAnswered ? 'answered' : 'pending'">
                    {{ q.isAnswered ? '답변완료' : '답변대기' }}
                  </span>
                </td>
                <td class="col-date">{{ formatDate(q.createdAt) }}</td>
              </tr>
              <tr v-if="expandedId === q.id" class="board-detail-row">
                <td colspan="5">
                  <div class="board-detail">
                    <div class="detail-section">
                      <h4 class="detail-label">문의 내용</h4>
                      <p class="detail-content">{{ q.content }}</p>
                    </div>
                    <div v-if="q.isAnswered && q.answerContent" class="detail-section detail-section--answer">
                      <h4 class="detail-label">답변</h4>
                      <p class="detail-content">{{ q.answerContent }}</p>
                    </div>
                    <div v-if="q.memberId === currentMemberId" class="detail-actions">
                      <button class="btn-detail-delete" @click.stop="deleteQuestion(q.id)">삭제</button>
                    </div>
                  </div>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>

      <!-- 작성 모달 -->
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal-box board-modal">
          <h3 class="modal-title">고객 문의 작성</h3>
          <div class="form-group">
            <label class="form-label">제목</label>
            <input v-model="form.title" type="text" class="form-input" placeholder="제목을 입력하세요" maxlength="200" />
          </div>
          <div class="form-group">
            <label class="form-label">내용</label>
            <textarea v-model="form.content" class="form-textarea" rows="6" placeholder="문의 내용을 입력하세요"></textarea>
          </div>
          <div class="form-group form-checkbox">
            <label>
              <input type="checkbox" v-model="form.isSecret" />
              <span>비밀글로 작성</span>
            </label>
          </div>
          <div class="modal-actions">
            <button class="btn-cancel" @click="showModal = false">취소</button>
            <button class="btn-confirm" @click="submitQuestion" :disabled="submitting">
              {{ submitting ? '등록 중...' : '등록' }}
            </button>
          </div>
        </div>
      </div>

      <div v-if="toast" class="toast-msg">{{ toast }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '../composables/useAuth.js'

const { isLoggedIn, loginId } = useAuth()
const questions = ref([])
const loading = ref(true)
const expandedId = ref(null)
const showModal = ref(false)
const submitting = ref(false)
const toast = ref('')
const currentMemberId = ref(null)

const form = ref({ title: '', content: '', isSecret: false })

function showToast(msg) {
  toast.value = msg
  setTimeout(() => { toast.value = '' }, 2500)
}

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

function getTitle(q) {
  if (q.isSecret && q.content === '비밀 문의입니다.') return '비밀글입니다.'
  return q.title
}

function toggleRow(id) {
  expandedId.value = expandedId.value === id ? null : id
}

function openWriteModal() {
  form.value = { title: '', content: '', isSecret: false }
  showModal.value = true
}

async function loadQuestions() {
  try {
    const res = await fetch('/v1/api/qna/board', { credentials: 'include' })
    if (res.ok) questions.value = await res.json()
  } catch {} finally {
    loading.value = false
  }
}

async function loadCurrentMember() {
  try {
    const res = await fetch('/v1/api/account/check', { credentials: 'include' })
    if (res.ok) {
      const data = await res.json()
      if (data.loggedIn) currentMemberId.value = data.id
    }
  } catch {}
}

async function submitQuestion() {
  if (!form.value.title.trim() || !form.value.content.trim()) {
    showToast('제목과 내용을 입력해주세요.')
    return
  }
  submitting.value = true
  try {
    const res = await fetch('/v1/api/qna', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ title: form.value.title, content: form.value.content, isSecret: form.value.isSecret })
    })
    if (res.ok) {
      showModal.value = false
      loadQuestions()
      showToast('문의가 등록되었습니다.')
    } else {
      showToast('오류가 발생했습니다.')
    }
  } catch {
    showToast('네트워크 오류가 발생했습니다.')
  } finally {
    submitting.value = false
  }
}

async function deleteQuestion(id) {
  if (!confirm('문의를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/qna/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) {
    expandedId.value = null
    loadQuestions()
    showToast('삭제되었습니다.')
  }
}

onMounted(() => {
  loadQuestions()
  loadCurrentMember()
})
</script>

<style scoped>
.board-page { min-height: 60vh; }

.board-hero {
  background: var(--color-primary);
  color: #fff;
  padding: 48px 0 40px;
  margin-bottom: 40px;
}
.board-hero-title { font-size: 2rem; font-weight: 800; margin-bottom: 8px; }
.board-hero-desc { font-size: 0.95rem; opacity: 0.8; }

.board-container { padding-bottom: 80px; }

.board-actions { display: flex; justify-content: flex-end; margin-bottom: 16px; }

.btn-write {
  display: inline-block;
  padding: 10px 24px;
  background: var(--color-primary);
  color: #fff;
  font-size: 0.9rem;
  font-weight: 600;
  border-radius: var(--radius-sm);
  cursor: pointer;
  border: none;
  transition: background var(--transition);
}
.btn-write:hover { background: #333; }

.board-table-wrap { border-top: 2px solid var(--color-primary); }

.board-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.board-table th {
  padding: 12px 14px;
  text-align: left;
  font-weight: 600;
  color: var(--color-text-muted);
  border-bottom: 1px solid var(--color-border);
  background: var(--color-bg-gray);
}

.board-table td {
  padding: 14px 14px;
  border-bottom: 1px solid var(--color-border);
  vertical-align: middle;
}

.col-num { width: 60px; text-align: center; color: var(--color-text-muted); }
.col-title { min-width: 200px; }
.col-author { width: 120px; }
.col-status { width: 100px; }
.col-date { width: 110px; color: var(--color-text-muted); font-size: 0.82rem; }

.board-row {
  cursor: pointer;
  transition: background var(--transition);
}
.board-row:hover td { background: var(--color-bg-gray); }

.secret-icon { font-size: 0.85rem; }

.qna-badge {
  font-size: 0.78rem;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 10px;
}
.qna-badge.answered { background: #d1fae5; color: #065f46; }
.qna-badge.pending { background: #fef3c7; color: #92400e; }

.board-detail-row td { padding: 0; }
.board-detail {
  padding: 24px;
  background: #f9f9f9;
  border-bottom: 1px solid var(--color-border);
}

.detail-section { margin-bottom: 20px; }
.detail-section--answer {
  background: #eff6ff;
  border-left: 3px solid #4f8ef7;
  padding: 16px;
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
}
.detail-label { font-size: 0.82rem; font-weight: 700; color: var(--color-text-muted); margin-bottom: 8px; text-transform: uppercase; letter-spacing: 0.05em; }
.detail-content { font-size: 0.9rem; line-height: 1.7; white-space: pre-wrap; }

.detail-actions { display: flex; justify-content: flex-end; margin-top: 8px; }
.btn-detail-delete {
  padding: 6px 16px;
  background: var(--color-sale);
  color: #fff;
  border: none;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  cursor: pointer;
}

.board-modal { width: 100%; max-width: 560px; }
.modal-box {
  background: #fff;
  border-radius: var(--radius-lg);
  padding: 32px;
  max-height: 90vh;
  overflow-y: auto;
}
.modal-title { font-size: 1.1rem; font-weight: 700; margin-bottom: 24px; }

.form-group { margin-bottom: 16px; }
.form-label { display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 6px; color: var(--color-text-muted); }
.form-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--color-border-dark);
  border-radius: var(--radius-sm);
  font-size: 0.9rem;
  outline: none;
}
.form-input:focus { border-color: var(--color-primary); }
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--color-border-dark);
  border-radius: var(--radius-sm);
  font-size: 0.9rem;
  resize: vertical;
  outline: none;
  font-family: inherit;
}
.form-textarea:focus { border-color: var(--color-primary); }

.form-checkbox label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.88rem;
  cursor: pointer;
}

.btn-confirm {
  flex: 2;
  padding: 12px;
  background: var(--color-primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-sm);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background var(--transition);
}
.btn-confirm:hover { background: #333; }
.btn-confirm:disabled { opacity: 0.6; cursor: not-allowed; }

.toast-msg {
  position: fixed;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0,0,0,0.82);
  color: #fff;
  padding: 12px 24px;
  border-radius: 24px;
  font-size: 0.88rem;
  z-index: 3000;
}
</style>
