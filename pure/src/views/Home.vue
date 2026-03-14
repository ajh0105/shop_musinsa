<template>

	<main>

		<!-- 메인 배너 -->

		<div style="margin:20px">

			<img
				src="/images/img1.jpg"
				style="width:100%"
			>

		</div>


		<!-- 히트상품 -->

		<section style="margin:20px">

			<h2>
				히트 상품
			</h2>

			<div class="pure-g">

				<div
					class="pure-u-1 pure-u-md-1-4"
					v-for="i in 4"
					:key="i"
				>

					<div style="padding:10px">

						<img
							:src="'/images/pro0'+i+'.png'"
							style="width:100%"
						>

						<h4>
							히트 상품 {{ i }}
						</h4>

						<p>
							히트 상품 설명
						</p>

					</div>

				</div>

			</div>

		</section>

	</main>

</template>

<script setup>

</script>