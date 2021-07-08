import {Box, IconButton, makeStyles, Typography} from "@material-ui/core";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {GET_EVENTS, getEvents, mergeSessions, updateEventEnd, updateEventStart} from "../../api/events";
import {
  Timeline,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
  TimelineItem, TimelineOppositeContent,
  TimelineSeparator
} from "@material-ui/lab";
import EventTimePicker from "./EventTimePicker";
import {CallMerge} from "@material-ui/icons";
import {GET_TASKS} from "../../api/tasks";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const useStyles = makeStyles(theme => ({
  taskName: {
    paddingTop: "2em"
  },
  lastTimelineItem: {
    height: "20px",
    minHeight: 0
  },
  hover: {
    '& .showMergeOnHover': {
      opacity: 0,
      transition: "200ms all"
    },
    '&:hover .showMergeOnHover': {
      opacity: 1
    }
  }
}))

const getStartTimePicker = (ev) => (
  <EventTimePicker selectedTime={new Date(ev.start * 1000)} eventId={ev.id}
                   updateFunction={updateEventStart}/>
)

const getEndTimePicker = (ev) => (
  <EventTimePicker selectedTime={new Date(ev.end * 1000)} eventId={ev.id}
                   updateFunction={updateEventEnd}/>
)

const DayHistoryCard = ({selectedDate}) => {
  const classes = useStyles()
  const queryClient = useQueryClient()

  const getEventsQuery = useQuery([GET_EVENTS, selectedDate], getEvents)

  const mergeSessionsQuery = useMutation(mergeSessions, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
      queryClient.invalidateQueries(GET_EVENTS)
    },
    onError: () => {
      toast.error("Unable to merge sessions")
    }
  })

  const handleMergeSessions = (eventId) => {
    mergeSessionsQuery.mutate(eventId)
  }
  return (
    <Box m={3}>
      <Typography variant={"h6"}>Day history</Typography>

      {getEventsQuery.isSuccess && getEventsQuery.data.sessions.map((session, index) =>
        <Box className={classes.hover}>
          <Timeline>
            {session.events.map(event =>
              <TimelineItem>
                <TimelineOppositeContent>
                  <Box>
                    {getStartTimePicker(event)}
                  </Box>
                </TimelineOppositeContent>
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
            <TimelineItem className={classes.lastTimelineItem}>
              <TimelineOppositeContent
                className={classes.lastTimelineItem}>{session.events[session.events.length - 1].end ? getEndTimePicker(session.events[session.events.length - 1]) : "Now"}</TimelineOppositeContent>
              <TimelineSeparator className={classes.lastTimelineItem}>
                <TimelineDot/>
              </TimelineSeparator>
              <TimelineContent className={classes.lastTimelineItem}> </TimelineContent>
            </TimelineItem>
          </Timeline>
          {index < getEventsQuery.data.sessions.length - 1 && <Box display={"flex"} justifyContent={"center"}>
            <IconButton size={"small"} className={'showMergeOnHover'}
                        onClick={() => handleMergeSessions(session.events[session.events.length - 1].id)}>
              <CallMerge/>
            </IconButton>
          </Box>}
        </Box>
      )}
    </Box>
  )
}

export default DayHistoryCard
