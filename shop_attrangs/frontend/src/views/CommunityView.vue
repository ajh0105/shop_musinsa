<template>
  <main class="page-wrap">
    <div class="community-header">
      <h2 class="community-title">커뮤니티</h2>
      <p class="community-subtitle">리뷰와 Q&amp;A를 통해 소통해 보세요</p>
    </div>

    <!-- 탭 -->
    <div class="comm-tabs">
      <button
        class="comm-tab"
        :class="{ active: activeTab === 'review' }"
        @click="activeTab = 'review'"
      >
        ★ Review
      </button>
      <button
        class="comm-tab"
        :class="{ active: activeTab === 'qna' }"
        @click="activeTab = 'qna'"
      >
        ✉ Q&amp;A
      </button>
    </div>

    <!-- 리뷰 탭 -->
    <div v-if="activeTab === 'review'" class="comm-content">
      <div class="comm-toolbar">
        <span class="comm-count">총 {{ reviews.length }}개</span>
      </div>

      <div v-if="reviews.length === 0" class="comm-empty">
        <p>아직 등록된 리뷰가 없습니다.</p>
        <p class="comm-empty-sub">첫 번째 리뷰를 작성해 보세요!</p>
      </div>

      <div v-else class="comm-list">
        <div v-for="r in reviews" :key="r.id" class="comm-item review-item">
          <div class="comm-item-head">
            <span class="comm-author">{{ r.memberName }}</span>
            <span class="comm-rating">{{ '★'.repeat(r.rating ?? 5) }}</span>
            <span class="comm-date">{{ formatDate(r.createdAt) }}</span>
          </div>
          <p class="comm-content-text">{{ r.content }}</p>
          <div v-if="r.itemId" class="comm-item-meta">상품 #{{ r.itemId }}</div>
        </div>
      </div>
    </div>

    <!-- Q&A 탭 -->
    <div v-if="activeTab === 'qna'" class="comm-content">
      <div class="comm-toolbar">
        <span class="comm-count">총 {{ questions.length }}개</span>
        <button v-if="isLoggedIn" class="btn-comm-write" @click="showWriteForm = !showWriteForm">
          {{ showWriteForm ? '취소' : '문의 작성' }}
        </button>
        <span v-else class="comm-login-hint">로그인 후 문의를 남길 수 있습니다</span>
      </div>

      <!-- 작성 폼 -->
      <div v-if="showWriteForm && isLoggedIn" class="comm-write-form">
        <h3 class="comm-form-title">상품 문의 작성</h3>
        <div class="form-group">
          <label class="form-label">제목</label>
          <input v-model="newQ.title" class="form-input" placeholder="문의 제목을 입력해 주세요" />
        </div>
        <div class="form-group">
          <label class="form-label">내용</label>
          <textarea v-model="newQ.content" class="comm-textarea" rows="4" placeholder="문의 내용을 입력해 주세요"></textarea>
        </div>
        <div class="comm-form-footer">
          <label class="comm-secret-label">
            <input type="checkbox" v-model="newQ.isSecret" />
            비밀 문의
          </label>
          <button class="btn-comm-submit" @click="submitQuestion">등록하기</button>
        </div>
      </div>

      <div v-if="questions.length === 0" class="comm-empty">
        <p>등록된 문의가 없습니다.</p>
      </div>

      <div v-else class="comm-list">
        <div v-for="q in questions" :key="q.id" class="comm-item qna-item">
          <div class="comm-item-head">
            <span class="comm-author">{{ q.memberName }}</span>
            <span class="comm-secret-badge" v-if="q.isSecret">🔒 비밀</span>
            <span class="comm-answered-badge" v-if="q.isAnswered">답변완료</span>
            <span class="comm-date">{{ formatDate(q.createdAt) }}</span>
          </div>
          <p class="comm-item-title">{{ q.title }}</p>
          <p class="comm-content-text">{{ q.content }}</p>
          <div v-if="q.isAnswered && q.answerContent" class="comm-answer">
            <span class="comm-answer-label">💬 답변</span>
            <p>{{ q.answerContent }}</p>
          </div>
        </div>
      </div>
    </div>

    <Transition name="toast">
      <div v-if="toast" class="toast">{{ toast }}</div>
    </Transition>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '../api'
import { useAuth } from '../composables/useAuth.js'

const { isLoggedIn } = useAuth()
const activeTab = ref('review')
const reviews = ref([])
const questions = ref([])
const showWriteForm = ref(false)
const toast = ref('')

const newQ = ref({ title: '', content: '', isSecret: false })

// Mock fallback data
const MOCK_REVIEWS = [
  { id: 1, memberName: '김지은', rating: 5, content: '원피스 색감이 너무 예뻐요! 사진보다 실물이 더 이쁩니다. 재구매 의사 있어요 ♥', itemId: 1001, createdAt: '2026-03-10T10:30:00' },
  { id: 2, memberName: '박수아', rating: 4, content: '배송이 빠르고 퀄리티도 좋아요. 사이즈는 생각보다 약간 작게 나와서 한 사이즈 업 추천드려요.', itemId: 1002, createdAt: '2026-03-08T14:22:00' },
  { id: 3, memberName: '이나연', rating: 5, content: '아뜨랑스는 역시 믿고 사는 브랜드! 원단이 부드럽고 착용감이 너무 편해요.', itemId: 1005, createdAt: '2026-03-05T09:15:00' },
  { id: 4, memberName: '최유진', rating: 4, content: '코트 디자인이 세련되고 보온성도 좋아요. 다음 시즌에도 꼭 구매할 것 같아요.', itemId: 1042, createdAt: '2026-03-02T16:40:00' },
  { id: 5, memberName: '정민지', rating: 5, content: '블라우스 소재가 정말 고급스럽고 착용했을 때 핏이 완벽해요! 강추합니다 :)', itemId: 1023, createdAt: '2026-02-28T11:00:00' },
]
const MOCK_QUESTIONS = [
  { id: 1, memberName: '이수진', title: '사이즈 문의드립니다', content: '평소 55 사이즈인데 어떤 사이즈 주문하면 될까요?', isSecret: false, isAnswered: true, answerContent: '안녕하세요! 평소 55 사이즈라면 S 사이즈를 추천드립니다. 넉넉하게 입고 싶으시면 M도 좋아요 :)', createdAt: '2026-03-12T10:00:00' },
  { id: 2, memberName: '박지현', title: '세탁 방법 문의', content: '드라이클리닝만 가능한가요, 아니면 손세탁도 되나요?', isSecret: false, isAnswered: false, answerContent: null, createdAt: '2026-03-14T14:30:00' },
  { id: 3, memberName: '김하늘', title: '비밀 문의입니다.', content: '비밀 문의입니다.', isSecret: true, isAnswered: false, answerContent: null, createdAt: '2026-03-15T09:20:00' },
  { id: 4, memberName: '윤서연', title: '재입고 예정 문의', content: '품절된 핑크 원피스 재입고 예정이 있나요? 너무 예뻐서 꼭 사고 싶어요!', isSecret: false, isAnswered: true, answerContent: '고객님 안녕하세요! 해당 상품은 다음달 초에 재입고 예정입니다. 입고 알림 신청해 두시면 빠르게 안내드릴게요!', createdAt: '2026-03-10T16:45:00' },
]

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

function showToast(msg) {
  toast.value = msg
  setTimeout(() => { toast.value = '' }, 2200)
}

async function loadReviews() {
  try {
    const { data } = await api.get('/reviews/all')
    if (Array.isArray(data) && data.length > 0) {
      reviews.value = data
      return
    }
  } catch { /* fall through */ }
  reviews.value = MOCK_REVIEWS
}

async function loadQuestions() {
  try {
    const { data } = await api.get('/qna/all')
    if (Array.isArray(data) && data.length > 0) {
      questions.value = data
      return
    }
  } catch { /* fall through */ }
  questions.value = MOCK_QUESTIONS
}

async function submitQuestion() {
  if (!newQ.value.title.trim() || !newQ.value.content.trim()) {
    showToast('제목과 내용을 입력해 주세요.')
    return
  }
  try {
    await api.post('/qna', {
      title: newQ.value.title,
      content: newQ.value.content,
      isSecret: newQ.value.isSecret
    })
    showToast('문의가 등록되었습니다.')
    newQ.value = { title: '', content: '', isSecret: false }
    showWriteForm.value = false
    await loadQuestions()
  } catch {
    showToast('문의 등록에 실패했습니다.')
  }
}

onMounted(() => {
  loadReviews()
  loadQuestions()
})
</script>

<style scoped>
.community-header {
  margin-bottom: 28px;
}
.community-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 6px;
  letter-spacing: -0.01em;
}
.community-subtitle {
  margin: 0;
  color: var(--sub);
  font-size: 14px;
}

/* 탭 */
.comm-tabs {
  display: flex;
  gap: 0;
  border-bottom: 2px solid var(--line);
  margin-bottom: 28px;
}
.comm-tab {
  padding: 12px 32px;
  font-size: 15px;
  font-weight: 600;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  margin-bottom: -2px;
  cursor: pointer;
  color: var(--sub);
  transition: all 0.18s;
  letter-spacing: 0.02em;
}
.comm-tab.active {
  color: var(--point);
  border-bottom-color: var(--point);
}
.comm-tab:hover:not(.active) {
  color: var(--text);
}

/* 툴바 */
.comm-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
}
.comm-count {
  font-size: 13px;
  color: var(--sub);
}
.comm-login-hint {
  font-size: 13px;
  color: #bbb;
}

/* 작성 버튼 */
.btn-comm-write {
  padding: 8px 18px;
  background: var(--point);
  color: #fff;
  border: none;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
.btn-comm-write:hover { background: #a03d1a; }

/* 작성 폼 */
.comm-write-form {
  background: #fff9f5;
  border: 1px solid var(--line);
  border-radius: 14px;
  padding: 24px;
  margin-bottom: 24px;
}
.comm-form-title {
  font-size: 16px;
  font-weight: 700;
  margin: 0 0 18px;
  color: var(--text);
}
.comm-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--line);
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  background: #fff;
}
.comm-textarea:focus { outline: none; border-color: var(--point); }
.comm-form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 14px;
}
.comm-secret-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--sub);
  cursor: pointer;
}
.btn-comm-submit {
  padding: 10px 24px;
  background: var(--point);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}
.btn-comm-submit:hover { background: #a03d1a; }

/* 목록 */
.comm-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.comm-item {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 14px;
  padding: 20px 24px;
  transition: box-shadow 0.18s;
}
.comm-item:hover { box-shadow: 0 4px 16px rgba(46,32,25,0.08); }

.comm-item-head {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}
.comm-author {
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
}
.comm-rating {
  color: #f59f00;
  font-size: 13px;
  letter-spacing: -0.05em;
}
.comm-date {
  font-size: 12px;
  color: #aaa;
  margin-left: auto;
}
.comm-item-title {
  font-size: 15px;
  font-weight: 600;
  margin: 0 0 6px;
  color: var(--text);
}
.comm-content-text {
  margin: 0;
  font-size: 14px;
  color: var(--sub);
  line-height: 1.6;
}
.comm-item-meta {
  margin-top: 8px;
  font-size: 11px;
  color: #bbb;
}
.comm-secret-badge {
  font-size: 11px;
  background: #f5f5f5;
  color: #888;
  padding: 2px 8px;
  border-radius: 999px;
}
.comm-answered-badge {
  font-size: 11px;
  background: #e8f5e9;
  color: #2e7d32;
  padding: 2px 8px;
  border-radius: 999px;
  font-weight: 600;
}
.comm-answer {
  margin-top: 14px;
  padding: 14px 16px;
  background: #fff8f5;
  border-left: 3px solid var(--point);
  border-radius: 0 8px 8px 0;
}
.comm-answer-label {
  display: block;
  font-size: 12px;
  font-weight: 700;
  color: var(--point);
  margin-bottom: 6px;
}
.comm-answer p {
  margin: 0;
  font-size: 14px;
  color: var(--text);
  line-height: 1.6;
}

/* 빈 상태 */
.comm-empty {
  text-align: center;
  padding: 60px 0;
  color: var(--sub);
}
.comm-empty-sub {
  font-size: 13px;
  color: #bbb;
  margin-top: 6px;
}
</style>
