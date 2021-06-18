import keycloak from "./keycloak";

const apiCall = (url, params) => {
  return keycloak.updateToken(10)
    .then(() => {
      const request = !params ? {} : params
      // const request = {}
      if (!request.headers) request.headers = {}
      request.credentials = "include"
      request.headers["Authorization"] = "Bearer " + keycloak.token
      request.headers["Content-Type"] = "application/json"
      return fetch(process.env.REACT_APP_API_URL + url, request)
    }).then(res => {
      if (!res.ok) throw Error(res.statusText)
      return res
    })
}

export default apiCall
