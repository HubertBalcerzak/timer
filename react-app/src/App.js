import {ReactKeycloakProvider} from "@react-keycloak/web";
import keycloak from "./keycloak";
import {BrowserRouter} from "react-router-dom";
import Main from "./pages/Main";
import {QueryClient, QueryClientProvider} from "react-query";

const queryClient = new QueryClient()

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider authClient={keycloak}>
        <BrowserRouter>
          <Main/>
        </BrowserRouter>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  );
}

export default App;
