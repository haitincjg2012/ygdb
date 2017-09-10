<template>
  <nav data-spm="h.0.0.401" class="bar bar-tab">
    <slot></slot>
  </nav>
</template>

<script>
  export default {
  }
</script>

<style scoped>
  .tab-item.active {
    color: #0bbe06 !important;
  }

  .tab-item.inactive {
  }
</style>
