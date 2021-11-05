<template>
  <div class="q-px-md q-gutter-sm">
    <q-chip class="glossy" icon="eva-file-add-outline">填写申请理由</q-chip>
    <q-editor
      v-model="noteVal"
      flat
      class="note"
      content="写下您的申请理由"
      max-height="150px"
      content-class="bg-blue-grey-1"
      toolbar-text-color="dark"
      toolbar-toggle-color="yellow-8"
      toolbar-bg="primary"
      :toolbar="[
        ['bold', 'italic', 'underline'],
        [
          {
            label: $q.lang.editor.formatting,
            icon: $q.iconSet.editor.formatting,
            list: 'no-icons',
            options: ['p', 'h3', 'h4', 'h5', 'h6', 'code']
          }
        ]
      ]"
      @paste.native="evt => pasteCapture(evt)"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      noteVal: "请详细填写..."
    };
  },
  methods: {
    /**
     * Capture the <CTL-V> paste event, only allow plain-text, no images.
     *
     */
    pasteCapture(evt) {
      let text, onPasteStripFormattingIEPaste;
      evt.preventDefault();
      if (evt.originalEvent && evt.originalEvent.clipboardData.getData) {
        text = evt.originalEvent.clipboardData.getData("text/plain");
        this.$refs.editor_ref.runCmd("insertText", text);
      } else if (evt.clipboardData && evt.clipboardData.getData) {
        text = evt.clipboardData.getData("text/plain");
        this.$refs.editor_ref.runCmd("insertText", text);
      } else if (window.clipboardData && window.clipboardData.getData) {
        if (!onPasteStripFormattingIEPaste) {
          onPasteStripFormattingIEPaste = true;
          this.$refs.editor_ref.runCmd("ms-pasteTextOnly", text);
        }
        onPasteStripFormattingIEPaste = false;
      }
    }
  },
  updated() {
    this.$emit("updateNoteValue", this.noteVal);
  }
};
</script>

<style scoped>
.note {
  width: 800px;
  height: 150px;
}
</style>
