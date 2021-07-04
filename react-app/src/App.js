import {ReactKeycloakProvider} from "@react-keycloak/web";
import keycloak from "./keycloak";
import {BrowserRouter} from "react-router-dom";
import Main from "./pages/Main";
import {QueryClient, QueryClientProvider} from "react-query";
import {createMuiTheme, ThemeProvider} from "@material-ui/core";
import {blue, teal} from "@material-ui/core/colors";

const queryClient = new QueryClient()

const theme = createMuiTheme({
  palette: {
    primary: teal,
    secondary: blue,
  },
})

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider authClient={keycloak}>
        <BrowserRouter>
          <ThemeProvider theme={theme}>
            <Main/>
          </ThemeProvider>
        </BrowserRouter>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  );
}

export default App;
