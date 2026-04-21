<template>
  <div class="sidebar">
    <h2 class="logo">TMS</h2>

    <nav class="nav">
      <router-link class="link" to="/">Home</router-link>
      <router-link class="link" to="/orders">Orders</router-link>
      <router-link class="link" to="/planning">Planning</router-link>
    </nav>
  </div>
</template>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background: #2c3e50;
  color: white;
  padding: 20px;

  display: flex;
  flex-direction: column;
}

.logo {
  margin-bottom: 30px;
  font-size: 22px;
}

.nav {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.link {
  color: white;
  text-decoration: none;
  padding: 10px;
  border-radius: 6px;
  transition: 0.2s;
}

.link:hover {
  background: #34495e;
}
</style>