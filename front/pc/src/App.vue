<template>
	<div id="app">
		<router-view />
	</div>
</template>



<style lang="scss">
	.el-submenu li {
		min-width: 0 !important;
	}

	html,
	body {
		padding: 0 0;
		margin: 0 0;
		width: 100%;
		min-height: 100%;
		font-size: 62.5%;
		overflow: hidden
	}

	#app {
		height: 100%;
	}

	#editor {
		height: 700px;
		border: 1px solid #ccc;
	}

	#toolbar {
		 border: 1px solid #ccc;
	}
</style>
