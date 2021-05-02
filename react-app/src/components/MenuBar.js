import {AppBar, Box, IconButton, makeStyles, Menu, MenuItem, Toolbar, Typography} from "@material-ui/core";
import {AccountCircle} from "@material-ui/icons";
import {useState} from "react";
import {useKeycloak} from "@react-keycloak/web";

const useStyles = makeStyles(theme => ({
  offset: theme.mixins.toolbar,
  title: {
    flexGrow: 1,
  }
}))

const MenuBar = () => {
  const classes = useStyles()
  const [anchorEl, setAnchorEl] = useState(null)

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null)
  }

  const {keycloak} = useKeycloak()
  const logout = () => {
    keycloak.logout()
  }

  return (
    <>
      <AppBar>
        <Toolbar>
          <Box className={classes.title}>
            <Typography variant={"h6"}>
              Timer
            </Typography>
          </Box>
          <Box>
            <IconButton color={"inherit"} onClick={handleClick}>
              <AccountCircle/>
            </IconButton>
            <Menu open={Boolean(anchorEl)}
                  keepMounted
                  anchorEl={anchorEl}
                  onClose={handleClose}>
              <MenuItem onClick={logout}>Logout</MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </AppBar>
      <Box className={classes.offset}/>
    </>
  )
}

export default MenuBar
