import {Box, Divider, IconButton, makeStyles, Typography} from "@material-ui/core";
import {PlayArrow} from "@material-ui/icons";

const useStyles = makeStyles(theme =>({
  flexGrow: {
    flexGrow: 1
  },
  row: {
    display: "flex",
    alignItems: "center",
    paddingBottom: theme.spacing(0.5),
    paddingTop: theme.spacing(0.5)
  }
}))

const TaskItem = ({taskName}) => {
  const classes = useStyles()

  return (
    <>
      <Box className={classes.row}>
        <Typography className={classes.flexGrow}>{taskName}</Typography>
        <IconButton>
          <PlayArrow/>
        </IconButton>
      </Box>
      <Divider/>
    </>
  )
}

export default TaskItem
