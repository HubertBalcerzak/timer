import keycloak from './keycloak'

const apiCall = async (url, params) => {
  keycloak.updateToken(10)

  const request = params ?? {}

  if (!request.headers) {
    request.headers = {}
  }
  request.credentials = 'include'
  request.headers['Authorization'] = 'Bearer ' + keycloak.token
  request.headers['Content-Type'] = 'application/json'

  const response = await fetch(process.env.REACT_APP_API_URL + url, request)

  if (!response.ok) {
    throw new Error(response.statusText)
  }

  return response
}

export default apiCall
