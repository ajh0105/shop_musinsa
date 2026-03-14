<template>

	<header>

		<div class="pure-menu pure-menu-horizontal">

			<a class="pure-menu-heading">
				<img src="/images/logo.png" alt="logo" style="width:120px; height:auto"/>
			</a>

			<ul class="pure-menu-list">

				<li class="pure-menu-item">
					<a
						href="/member/login"
						class="pure-menu-link"
					>
						로그인
					</a>
				</li>

				<li class="pure-menu-item">
					<a
						href="/member/join"
						class="pure-menu-link"
					>
						회원가입
					</a>
				</li>

				<li class="pure-menu-item">
					<a
						href="/cart/list"
						class="pure-menu-link"
					>
						장바구니
					</a>
				</li>

				<li class="pure-menu-item">
					<a
						href="/member/mypage"
						class="pure-menu-link"
					>
						마이페이지
					</a>
				</li>

			</ul>

		</div>


		<!-- 검색 -->

		<div style="margin:20px">

			<form class="pure-form">

				<input
					type="search"
					placeholder="검색어를 입력하세요"
				>

				<button class="pure-button pure-button-primary">
					검색
				</button>

			</form>

		</div>

	</header>

</template>

<script setup>

</script>