// utils/apiUtils.js
import axios from "axios";
import { useUiStore } from "@/common/stores/uiStore";
import { useAlertStore } from "../stores/alertStore";

export async function apiExecutar({
  method = "get",
  url,
  data,
  params,
  headers,

  showSuccess = true,
  successMessage = null,
  showError = true,
  errorMessage = null
} = {}) {

  const ui = useUiStore();
  const alert = useAlertStore();

  ui.startLoading();

  await new Promise((resolve, reject) => { 
          setTimeout(() => { resolve({status: 'OK'}) }, 5500);
        }).then(() => {});

  try {
    const response = await axios({
      method,
      url,
      data,
      params,
      headers
    });

    if (showSuccess) {
      await alert.alert({
        title: "Aviso",
        message: successMessage || "Operación realizada correctamente",
      });
    }

    return response.data;

  } catch (error) {

    if (showError) {
      await alert.alert({
        title: "Error",
        message: errorMessage || error.message,
      });
    }

    throw error;

  } finally {
    ui.hideLoading();
  }
}

export function apiExecutarV1({
  tryFetch: {
    url,
    data,
    query,
    headers,
  } = {},

  alertFetch = {
    showSuccess: true,
    titleSuccess: "Aviso",
    messageSuccess: "Petición realizada exitosamente.",
    showError: true,
    titleError: "Aviso",
    messageError: "Hubo un error en el servicio.",
  },

  catchFn,
  finallyFn,
} = {}) {
  return new Promise( (resolve, reject) => {
    setTimeout( async () => {
      const alert = useAlertStore();

      try {
        if (alertFetch.showSuccess) {
          await alert.alert({
            title: alertFetch.titleSuccess,
            message: alertFetch.messageSuccess
          });
        }

        resolve({
          status: "success",
          message: "Correctamente."
        });

      } catch (error) {

        if (alertFetch.showError) {
          await alert.alert({
            title: alertFetch.titleError,
            message: alertFetch.messageError
          });
        }

        if (typeof catchFn === "function") {
          catchFn(error);
        } else {
          reject({
            status: "error",
            message: error.message
          });
        }

      } finally {

        if (typeof finallyFn === "function") {
          finallyFn();
        }

      }
    }, 4500);
  });
}

export function apiExecutarV2({
  data = null,
  showSuccess = true,
  successMessage = "Petición realizada exitosamente.",
  errorMessage = "Hubo un error en el servicio.",
  finallyFn
} = {}) {

  return new Promise((resolve, reject) => {

    setTimeout( async () => {
      const alert = useAlertStore();

      try {

        if (showSuccess) {
          await alert.alert({
            title: "Aviso",
            message: successMessage
          });
        }

        resolve({
          status: "success",
          data
        });

      } catch (error) {

        await alert.alert({
          title: "Error",
          message: errorMessage
        });

        reject({
          status: "error",
          message: error.message
        });

      } finally {

        if (typeof finallyFn === "function") {
          finallyFn();
        }

      }
    }, 4500);

  });
}

export async function apiExecutarV3({
  data = null,
  showSuccess = true,
  successMessage = "Petición realizada exitosamente.",
  errorMessage = "Hubo un error en el servicio."
} = {}) {

  try {

    const alert = useAlertStore();
    await new Promise(r => setTimeout(r, 4500));


    if (showSuccess) {
      await alert.alert({
        title: "Aviso",
        message: successMessage
      });
    }

    return { status: "success", data };

  } catch (error) {

    await alert.alert({
      title: "Error",
      message: errorMessage
    });

    throw error;
  }
}
