import {AppBar, Box, CssBaseline, IconButton, makeStyles, Toolbar, Typography} from "@material-ui/core";
import {AccountCircle} from "@material-ui/icons";

const useStyles = makeStyles(theme => ({
  offset: theme.mixins.toolbar,
  title: {
    flexGrow: 1,
  }
}))

const MenuBar = () => {
  const classes = useStyles()
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
            <IconButton>
              <AccountCircle/>
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>
    </>
  )
}

export default MenuBar
