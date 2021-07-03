import {Box, makeStyles, Typography} from "@material-ui/core";
import {useQuery, useQueryClient} from "react-query";
import {GET_EVENTS, getEvents} from "../../api/events";
import {
  Timeline,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
  TimelineItem, TimelineOppositeContent,
  TimelineSeparator
} from "@material-ui/lab";
import {format} from "date-fns";

const useStyles = makeStyles(theme => ({
  taskName: {
    paddingTop: "2em"
  }
}))

const formatDate = (epochSeconds) => {
  return format(new Date(epochSeconds * 1000), 'HH:mm:ss')
}

const DayHistoryCard = ({selectedDate}) => {
  const classes = useStyles()
  const queryClient = useQueryClient()

  const getEventsQuery = useQuery([GET_EVENTS, selectedDate], getEvents)


  return (
    <Box m={3}>
      <Typography variant={"h6"}>Day history</Typography>

      {getEventsQuery.isSuccess && getEventsQuery.data.sessions.map(session =>
        <Timeline>
          {session.events.map(event =>
            <TimelineItem>
              <TimelineOppositeContent>{formatDate(event.start)}</TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineDot/>
                <TimelineConnector/>
              </TimelineSeparator>
              <TimelineContent>
                <Box className={classes.taskName}>
                  {event.taskName}
                </Box>
              </TimelineContent>
            </TimelineItem>)}
          <TimelineItem>
            <TimelineOppositeContent>{session.events[session.events.length - 1].end ? formatDate(session.events[session.events.length - 1].end) : "Now"}</TimelineOppositeContent>
            <TimelineSeparator>
              <TimelineDot/>
            </TimelineSeparator>
            <TimelineContent> </TimelineContent>
          </TimelineItem>
        </Timeline>
      )}
    </Box>
  )
}

export default DayHistoryCard
