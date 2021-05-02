import {ReactKeycloakProvider} from "@react-keycloak/web";
import keycloak from "./keycloak";
import {BrowserRouter} from "react-router-dom";
import Main from "./pages/Main";

function App() {
  return (
    <ReactKeycloakProvider authClient={keycloak}>
      <BrowserRouter>
        <Main/>
      </BrowserRouter>
    </ReactKeycloakProvider>
  );
}

export default App;
