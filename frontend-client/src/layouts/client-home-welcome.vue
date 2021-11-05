<template>
  <div class="q-py-md">
    <q-carousel
    style="height:auto"
      animated
      v-model="slide"
      navigation
      infinite
      control-color="amber"
      :autoplay="autoplay"
      arrows
      transition-prev="slide-right"
      transition-next="slide-left"
      @mouseenter="autoplay = true"
      @mouseleave="autoplay = false"
    >
      <q-carousel-slide :name="1" class="q-pa-none">
        <q-img
        class="my-scale"
        style="height: 490px"
        basic
          src="~assets/index1.png"
          spinner-color="primary"
          spinner-size="82px"
        />
      </q-carousel-slide>
      <q-carousel-slide :name="2" class="q-pa-none">
      <q-img
      class="my-scale"
      style="height: 490px"
        basic
          src="~assets/index2.png"
          spinner-color="primary"
          spinner-size="82px"
        />
      </q-carousel-slide>
      <q-carousel-slide :name="3" class="q-pa-none">
      <q-img
      class="my-scale"
      style="height: 490px"
        basic
          src="~assets/index3.png"
          spinner-color="primary"
          spinner-size="82px"
        />
      </q-carousel-slide>
      <q-carousel-slide :name="4" class="q-pa-none">
      <q-img
      class="my-scale"
      style="height: 490px"
        basic
          src="~assets/index4.png"
          spinner-color="primary"
          spinner-size="82px"
        />
      </q-carousel-slide>
    </q-carousel>
  </div>
</template>
<script>
export default {
  data () {
    return {
      slide: 1,
      autoplay:false
    }
  }
}
</script>

<style scoped>
.my-scale {
  animation: my-scale-in 3s;
}
@keyframes my-scale-in {
  0% {
    transform: scale(0.85);
  }
  100% {
    transform: scale(1);
  }
}
</style>
