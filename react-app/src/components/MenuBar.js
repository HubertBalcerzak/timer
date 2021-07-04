import {AppBar, Box, IconButton, makeStyles, Menu, MenuItem, Tab, Tabs, Toolbar, Typography} from "@material-ui/core";
import {AccountCircle} from "@material-ui/icons";
import {useState} from "react";
import {useKeycloak} from "@react-keycloak/web";
import {useSearchParam} from "react-use";

const useStyles = makeStyles(theme => ({
  offset: theme.mixins.toolbar,
  tabs: {
    flexGrow: 1,
    // height:"64px"
  }
}))

const MenuBar = ({tab, setTab}) => {
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
        {/*<Toolbar>*/}
        <Box display={"flex"} alignItems={"center"}>

          <Box ml={2} mr={2}>
            <Typography variant={"h6"}>
              Timer
            </Typography>
          </Box>
          <Box className={classes.tabs} display={"flex"} alignItems={"end"}>
            <Tabs
              textColor={"inherit"}
              indicatorColor={"secondary"}
              value={tab}
              onChange={(event, newValue) => setTab(newValue)}
            >
              <Tab label={"Today"}/>
              <Tab label={"History"}/>
              <Tab label={"Tasks"}/>
            </Tabs>
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
        </Box>
        {/*</Toolbar>*/}
      </AppBar>
      <Box className={classes.offset}/>
    </>
  )
}

export default MenuBar
