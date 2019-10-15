<template>
  <div></div>
</template>

<script>
  export default {
    name: "userInfo",
    data() {
      return {
        contentId: '',
      }
    },
    methods: {
      getContentId() {
        this.contentId = this.$route.params.id
        console.log(this.contentId)
      }
    },
    created() {
      this.getContentId()
    },
  }
</script>

<style scoped>

</style>
