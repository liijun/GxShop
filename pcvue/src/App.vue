<template>
    <div id="app">
        <router-view></router-view>
        <!-- <HelloWorld msg="Welcome to Your Vue.js App" /> -->
    </div>
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";

export default {
    name: "app",
    components: {
        // HelloWorld
    }
};
</script>

<style>
html,body,#app {
    height: 100%;
    margin:0;
    font-family: "Avenir", Helvetica, Arial, sans-serif;
}
ol,ul {list-style:none} 
a{text-decoration: none;color:#333;}
.qingwu{height: 100%;}
</style>
