import {useKeycloak} from "@react-keycloak/web";
import Dashboard from "./Dashboard";
import Login from "./Login";
import {CssBaseline} from "@material-ui/core";

const Main = () => {
  const {keycloak} = useKeycloak()
  return (
    <>
      <CssBaseline/>
      {keycloak.authenticated ? <Dashboard/> : <Login/>}
    </>
  )
}

export default Main
