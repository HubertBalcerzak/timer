import { ReactKeycloakProvider } from '@react-keycloak/web'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from 'react-query'
import { MuiPickersUtilsProvider } from '@material-ui/pickers'
import DateFnsUtils from '@date-io/date-fns'
import { ToastContainer } from 'react-toastify'
import Main from './pages/Main'
import keycloak from './keycloak'

const queryClient = new QueryClient()

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider authClient={keycloak}>
        <BrowserRouter>
          <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Main />
            <ToastContainer />
          </MuiPickersUtilsProvider>
        </BrowserRouter>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  )
}

export default App
