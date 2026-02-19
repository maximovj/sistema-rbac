// src/components/permisos/composables/usePermisoForm.js
import { ref, computed } from "vue";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";

export function usePermisoForm(permiso) {
  const visible = ref(false);

  // Esquema de validación
  const schema = yup.object({
    accion: yup
      .string()
      .required("El campo acción es obligatorio")
      .min(3, "Mínimo 3 caracteres")
      .max(20, "Máximo 20 caracteres")
      .matches(/^[A-Z_]+$/, "Solo letras mayúsculas y guion bajo"),
    modulo: yup
      .string()
      .required("El campo módulo es obligatorio")
      .min(3, "Mínimo 3 caracteres")
      .max(20, "Máximo 20 caracteres")
      .matches(/^MODULO_[A-Z_]+$/, "Debe comenzar con 'MODULO_' y solo letras mayúsculas y guion bajo")
  });

  // Inicializamos el formulario
  const { handleSubmit, resetForm, errors } = useForm({
    validationSchema: schema,
    initialValues: {
      accion: permiso.accion || "",
      modulo: permiso.modulo || ""
    }
  });

  // Campos individuales
  const { value: accion } = useField("accion");
  const { value: modulo } = useField("modulo");

  // Computed para cambios y vacío
  const formularioVacio = computed(() => !accion.value.trim() && !modulo.value.trim());
  const formularioConCambios = computed(() => {
    return accion.value.trim() !== (permiso.accion || "").trim() ||
           modulo.value.trim() !== (permiso.modulo || "").trim();
  });

  const abrirFormulario = () => {
    visible.value = true;
    resetForm({ values: { accion: permiso.accion || "", modulo: permiso.modulo || "" } });
  };

  const guardarFormulario = (emit) =>
    handleSubmit(() => {
      emit("guardar", {
        datos: { accion: accion.value, modulo: modulo.value },
        esVacio: formularioVacio.value,
        esDiferente: formularioConCambios.value
      });
      visible.value = false;
    });

  const cancelarFormulario = (emit) => {
    emit("cancelar");
    visible.value = false;
  };

  return {
    visible,
    accion,
    modulo,
    errors,
    formularioVacio,
    formularioConCambios,
    abrirFormulario,
    guardarFormulario,
    cancelarFormulario
  };
}
