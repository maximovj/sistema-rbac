// src/components/permisos/composables/useUsuarioForm.js
import { ref, computed } from "vue";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";

export function useUsuarioForm(usuarioForm) {
  const visible = ref(false);

  // Esquema de validación
  const schema = yup.object({
    usuario: yup
      .string()
      .required("El campo usuario es obligatorio")
      .min(3, "Mínimo 3 caracteres")
      .max(20, "Máximo 20 caracteres")
      .matches(/^[a-zA-Z][a-zA-Z0-9._-]*$/, "Debe comenzar con una letra y solo puede contener letras, números, puntos, guiones bajos o medios"),
    correo: yup
      .string()
      .required("El campo correo es obligatorio")
      .min(3, "Mínimo 3 caracteres")
      .max(20, "Máximo 20 caracteres")
      .email("El campo deber ser un correo electrónico"),
    grupo: yup
      .number()
      .required("Selecciona un grupo")
  });

  // Inicializamos el formulario
  const { handleSubmit, resetForm, errors } = useForm({
    validationSchema: schema,
    initialValues: {
      usuario: usuarioForm.usuario || "",
      correo: usuarioForm.correo || "",
      es_activo: null,
      grupo: null,
      rol: null,
    }
  });

  // Campos individuales
  const { value: usuario } = useField("usuario");
  const { value: correo } = useField("correo");
  const { value: es_activo } = useField("es_activo");
  const { value: rol } = useField("rol");
  const { value: grupo } = useField("grupo");

  // Computed para cambios y vacío
  const formularioVacio = computed(() => !usuario.value.trim() && !correo.value.trim());
  const formularioConCambios = computed(() => {
    return usuario.value.trim() !== (usuarioForm.usuario || "").trim() ||
           correo.value.trim() !== (usuarioForm.correo || "").trim();
  });

  const abrirFormulario = () => {
    visible.value = true;
    resetForm({ values: { 
        usuario: usuarioForm.usuario || "", 
        correo: usuarioForm.correo || "", 
        es_activo: usuarioForm.es_activo || "",
        rol: usuarioForm.rol || "",
        grupo: usuarioForm.grupo || "",
      } 
    });
  };

  const guardarFormulario = (emit) =>
    handleSubmit(() => {
      emit("guardar", {
        datos: { 
          usuario: usuario.value, 
          correo: correo.value, 
          es_activo: es_activo.value, 
          rol: rol.value, 
          grupo: grupo.value
        },
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
    usuario,
    correo,
    es_activo,
    rol,
    grupo,
    errors,
    formularioVacio,
    formularioConCambios,
    abrirFormulario,
    guardarFormulario,
    cancelarFormulario
  };
}
