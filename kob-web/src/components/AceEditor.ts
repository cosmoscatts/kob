import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'

/** 代码编辑器 */
ace.config.set(
  'basePath',
  `https://cdn.jsdelivr.net/npm/ace-builds@${ace.version}/src-noconflict/`)

export default {
  components: {
    VAceEditor,
  },
}
