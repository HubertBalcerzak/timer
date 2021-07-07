import {ReactKeycloakProvider} from "@react-keycloak/web";
import keycloak from "./keycloak";
import {BrowserRouter} from "react-router-dom";
import Main from "./pages/Main";
import {QueryClient, QueryClientProvider} from "react-query";
import {MuiPickersUtilsProvider} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import {ToastContainer} from "react-toastify";

const queryClient = new QueryClient()

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider authClient={keycloak}>
        <BrowserRouter>
          <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Main/>
            <ToastContainer/>
          </MuiPickersUtilsProvider>
        </BrowserRouter>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  );
}

export default App;
