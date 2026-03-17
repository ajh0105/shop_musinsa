<template>
  <footer class="site-footer">
    <div class="footer-inner">
      <!-- 좌측: 회사 정보 & 네비게이션 -->
      <div class="footer-left">
        <div class="footer-logo">MUSINSA</div>
        <div class="footer-links">
          <div class="footer-link-group">
            <span class="footer-link-title">고객지원</span>
            <RouterLink to="/faq" class="footer-link">FAQ</RouterLink>
            <RouterLink to="/board" class="footer-link">1:1 문의</RouterLink>
            <RouterLink to="/reviews" class="footer-link">전체 리뷰</RouterLink>
          </div>
          <div class="footer-link-group">
            <span class="footer-link-title">회사소개</span>
            <a href="#" class="footer-link">무신사 소개</a>
            <a href="#" class="footer-link">채용 정보</a>
            <a href="#" class="footer-link">이용약관</a>
          </div>
          <div class="footer-link-group">
            <span class="footer-link-title">서비스</span>
            <a href="#" class="footer-link">무신사 스토어</a>
            <a href="#" class="footer-link">무신사 매거진</a>
            <a href="#" class="footer-link">브랜드 입점</a>
          </div>
        </div>
        <div class="footer-company-info">
          <p><strong>주식회사 무신사</strong> | 대표이사 : 한문일</p>
          <p>사업자등록번호 : 220-88-12596 | 통신판매업신고 : 제2019-서울성동-00052호</p>
          <p>주소 : 서울특별시 성동구 아차산로 7길 15 (성수동2가) 무신사 빌딩</p>
          <p>개인정보보호 책임자 : 이석민 | 이메일 : privacy@musinsa.com</p>
        </div>
      </div>

      <!-- 우측: 고객센터 & SNS -->
      <div class="footer-right">
        <div class="footer-cs">
          <h4 class="footer-cs-title">고객센터</h4>
          <p class="footer-cs-number">1544-0181</p>
          <p class="footer-cs-hours">평일 11:00 ~ 19:00</p>
          <p class="footer-cs-hours">주말 및 공휴일 휴무</p>
          <div class="footer-cs-buttons">
            <RouterLink to="/board" class="btn-cs">1:1 문의하기</RouterLink>
            <RouterLink to="/faq" class="btn-cs">FAQ 바로가기</RouterLink>
          </div>
        </div>

        <div class="footer-sns">
          <h4 class="footer-sns-title">SNS</h4>
          <div class="footer-sns-icons">
            <a href="#" class="sns-icon" title="Instagram">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/>
              </svg>
            </a>
            <a href="#" class="sns-icon" title="YouTube">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"/>
              </svg>
            </a>
            <a href="#" class="sns-icon" title="Facebook">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
              </svg>
            </a>
          </div>
        </div>

        <div class="footer-payment">
          <h4 class="footer-payment-title">결제 수단</h4>
          <div class="footer-payment-methods">
            <span class="payment-badge">신용카드</span>
            <span class="payment-badge">무통장입금</span>
            <span class="payment-badge">카카오페이</span>
            <span class="payment-badge">네이버페이</span>
            <span class="payment-badge">토스페이</span>
          </div>
        </div>
      </div>
    </div>

    <div class="footer-bottom">
      <p>© 2024 MUSINSA Corp. All rights reserved.</p>
    </div>
  </footer>
</template>

<script setup>
</script>
