<template>
  <div class="admin-page">
    <h1 class="admin-page-title">FAQ 관리</h1>

    <div class="admin-toolbar">
      <button class="btn-admin-primary" @click="openAddModal">+ FAQ 추가</button>
    </div>

    <div class="admin-card">
      <table class="admin-table">
        <thead>
          <tr>
            <th>ID</th><th>카테고리</th><th>질문</th><th>순서</th><th>등록일</th><th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="f in faqs" :key="f.id">
            <td>{{ f.id }}</td>
            <td><span class="faq-cat-badge">{{ f.category }}</span></td>
            <td class="content-cell">{{ f.question }}</td>
            <td>{{ f.sortOrder }}</td>
            <td>{{ formatDate(f.createdAt) }}</td>
            <td class="action-cell">
              <button class="btn-admin-primary" @click="openEditModal(f)">수정</button>
              <button class="btn-admin-danger" @click="deleteFaq(f.id)">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 추가/수정 모달 -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-box">
        <h3>{{ editTarget ? 'FAQ 수정' : 'FAQ 추가' }}</h3>
        <div class="faq-form">
          <div class="faq-form-group">
            <label>카테고리</label>
            <select v-model="form.category" class="grade-select" style="width:100%">
              <option v-for="c in categories" :key="c" :value="c">{{ c }}</option>
            </select>
          </div>
          <div class="faq-form-group">
            <label>질문</label>
            <input v-model="form.question" type="text" class="admin-search" style="width:100%" placeholder="질문을 입력하세요" />
          </div>
          <div class="faq-form-group">
            <label>답변</label>
            <textarea v-model="form.answer" class="review-textarea" rows="5" placeholder="답변 내용을 입력하세요" style="width:100%;padding:8px;border:1px solid #ddd;border-radius:4px;font-size:14px;resize:vertical;" />
          </div>
          <div class="faq-form-group">
            <label>정렬 순서</label>
            <input v-model.number="form.sortOrder" type="number" class="admin-search" style="width:100px" placeholder="0" />
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showModal = false">취소</button>
          <button class="btn-confirm" @click="submitFaq">{{ editTarget ? '수정' : '등록' }}</button>
        </div>
      </div>
    </div>

    <div v-if="toast" class="toast-msg">{{ toast }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const faqs = ref([])
const showModal = ref(false)
const editTarget = ref(null)
const toast = ref('')

const categories = ['일반', '주문/결제', '배송', '교환/반품', '회원/계정', '기타']

const form = ref({ category: '일반', question: '', answer: '', sortOrder: 0 })

function showToast(msg) {
  toast.value = msg
  setTimeout(() => { toast.value = '' }, 2000)
}

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

async function loadFaqs() {
  try {
    const res = await fetch('/v1/api/admin/faq', { credentials: 'include' })
    if (res.ok) faqs.value = await res.json()
  } catch {}
}

function openAddModal() {
  editTarget.value = null
  form.value = { category: '일반', question: '', answer: '', sortOrder: 0 }
  showModal.value = true
}

function openEditModal(f) {
  editTarget.value = f
  form.value = { category: f.category, question: f.question, answer: f.answer, sortOrder: f.sortOrder }
  showModal.value = true
}

async function submitFaq() {
  if (!form.value.question.trim() || !form.value.answer.trim()) {
    showToast('질문과 답변을 모두 입력해주세요.')
    return
  }
  const url = editTarget.value ? `/v1/api/admin/faq/${editTarget.value.id}` : '/v1/api/admin/faq'
  const method = editTarget.value ? 'PUT' : 'POST'
  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(form.value)
  })
  if (res.ok) {
    showModal.value = false
    loadFaqs()
    showToast(editTarget.value ? '수정되었습니다.' : '등록되었습니다.')
  } else {
    showToast('오류가 발생했습니다.')
  }
}

async function deleteFaq(id) {
  if (!confirm('FAQ를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/admin/faq/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) { loadFaqs(); showToast('삭제되었습니다.') }
}

onMounted(loadFaqs)
</script>

<style scoped>
.faq-cat-badge {
  background: #e8f4fd;
  color: #1a56db;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 0.78rem;
  font-weight: 600;
  white-space: nowrap;
}

.faq-form { display: flex; flex-direction: column; gap: 14px; margin: 16px 0; }
.faq-form-group { display: flex; flex-direction: column; gap: 4px; }
.faq-form-group label { font-size: 0.82rem; font-weight: 600; color: #555; }

.modal-box {
  background: #fff;
  border-radius: 10px;
  padding: 28px;
  width: 100%;
  max-width: 520px;
  max-height: 90vh;
  overflow-y: auto;
}
.modal-box h3 { font-size: 1.1rem; font-weight: 700; margin-bottom: 4px; }

.btn-confirm {
  flex: 2;
  padding: 10px;
  background: #4f8ef7;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
}

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
