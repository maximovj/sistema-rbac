// common/stores/alertStore.js
import { defineStore } from "pinia";

export const useAlertStore = defineStore("alert", {
  state: () => ({
    visible: false,
    title: "",
    message: "",
    dark: false,

    type: null, // 'alert' | 'confirm' | 'prompt'
    resolver: null,

    // Prompt
    promptValue: "",
    promptPlaceholder: "",

    // Confirm buttons
    confirmButtons: {
      yes: "SI",
      no: "NO",
      cancel: "CANCELAR",
      visible: {
        yes: true,
        no: true,
        cancel: true
      }
    }
  }),

  actions: {
    // ===== BASE =====
    openBase({ title, message, dark }) {
      this.visible = true;
      this.title = title;
      this.message = message;
      this.dark = dark;

      return new Promise(resolve => {
        this.resolver = resolve;
      });
    },

    // ===== ALERT =====
    alert({ title = "Alerta", message, dark = false }) {
      this.type = "alert";
      return this.openBase({ title, message, dark });
    },

    // ===== CONFIRM =====
    confirm({ title = "Confirmación", message, dark = false, buttons = {} }) {
      this.type = "confirm";

      const visibles = buttons.visible || ["yes", "no", "cancel"];

      this.confirmButtons = {
        yes: buttons.yes || "SI",
        no: buttons.no || "NO",
        cancel: buttons.cancel || "CANCELAR",
        visible: {
          yes: visibles.includes("yes"),
          no: visibles.includes("no"),
          cancel: visibles.includes("cancel")
        }
      };

      return this.openBase({ title, message, dark });
    },

    // ===== PROMPT =====
    prompt({ title = "INGRESAR VALOR", message, placeholder = "", dark = false }) {
      this.type = "prompt";
      this.promptValue = "";
      this.promptPlaceholder = placeholder;

      return this.openBase({ title, message, dark });
    },

    // ===== CLOSE =====
    resolve(value) {
      this.visible = false;

      if (this.resolver) {
        this.resolver(value);
        this.resolver = null;
      }
    },

    cancel() {
      if (this.type === "confirm") this.resolve(false);
      else if (this.type === "prompt") this.resolve(null);
      else this.resolve(true);
    }
  }
});
