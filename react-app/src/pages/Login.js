import {Box, Button, Card, Container, makeStyles, Typography} from "@material-ui/core";
import {useCallback} from "react";
import {useKeycloak} from "@react-keycloak/web";

const useStyles = makeStyles(theme => ({
  container: {
    height: "100vh",
    display: "flex",
    alignItems: "center",
    justifyContent: "center"
  },
  card: {
    width: "30vw",
    height: "20vh",
    minHeight: "15em",
    minWidth: "20em",
    display: "flex",
    flexDirection: "column",
    padding: theme.spacing(2)
  },

  flexGrow: {
    flexGrow: 1,
  }
}))

const Login = () => {
  const classes = useStyles()
  const {keycloak} = useKeycloak()
  const login = useCallback(() => keycloak.login(), [keycloak])

  return (
    <Container className={classes.container}>
      <Card className={classes.card}>
        <Box className={classes.flexGrow} display={"flex"} justifyContent={"center"}>
          <Typography variant={"h5"}>
            Sign in to continue
          </Typography>
        </Box>
        <Button variant={"contained"} color={"primary"} onClick={login}>Sign in with Keycloak</Button>
      </Card>

    </Container>
  )
}

export default Login
