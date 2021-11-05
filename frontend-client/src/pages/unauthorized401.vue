<template>
  <q-layout>
    <q-page-container>
      <q-page class="fullscreen flex flex-center">
        <q-img src="https://cdn.quasar.dev/img/parallax2.jpg">
          <div class="absolute-full text2 flex flex-center">
            UNAUTHORIZED 401
          </div>
          <div
            class="column justify-start q-gutter-xl absolute-full flex flex-center"
          >
            <span />
            <div>
              <q-btn
                class="q-mt-xl"
                color="white"
                text-color="blue"
                unelevated
                to="/"
                label="Go Home"
                no-caps
              />
            </div>
          </div>
        </q-img>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  name: "unauthorized"
};
</script>
