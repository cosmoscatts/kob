<script setup lang="ts">

</script>

<template>
  <header class="header z-40">
    <h1>
      King Of Bots
    </h1>
    <nav class="nav">
      <div class="spacer" />
      <div class="right">
        <a href="https://twitter.com/antfu7" target="_blank" title="Twitter" class="lt-md:hidden">
          <feather-twitter />
        </a>
        <a href="https://github.com/antfu" target="_blank" title="GitHub" class="lt-md:hidden">
          <div i-uil-github-alt />
        </a>
      </div>
    </nav>
  </header>
</template>

<style scoped>
.header h1 {
  margin-bottom: 0;
}
.logo {
  position: absolute;
  top: 1.5rem;
  left: 1.5rem;
}
.nav {
  padding: 2rem;
  width: 100%;
  display: grid;
  grid-template-columns: auto max-content;
  box-sizing: border-box;
}
.nav > * {
  margin: auto;
}
.nav img {
  margin-bottom: 0;
}
.nav a {
  cursor: pointer;
  text-decoration: none;
  color: inherit;
  transition: opacity 0.2s ease;
  opacity: 0.6;
  outline: none;
}
.nav a:hover {
  opacity: 1;
  text-decoration-color: inherit;
}
.nav .right {
  display: grid;
  grid-gap: 1.2rem;
  grid-auto-flow: column;
}
.nav .right > * {
  margin: auto;
}
</style>
