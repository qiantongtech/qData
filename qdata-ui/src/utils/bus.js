// bus.js
import { reactive } from 'vue';

const bus = reactive({
  events: {},
  
  on(event, callback) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  },

  off(event, callback) {
    const index = this.events[event]?.indexOf(callback);
    if (index !== -1) {
      this.events[event].splice(index, 1);
    }
  },

  emit(event, data) {
    this.events[event]?.forEach(callback => callback(data));
  }
});

export default bus;
