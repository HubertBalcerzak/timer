import MenuBar from "../components/MenuBar";
import {Box, Card, makeStyles} from "@material-ui/core";
import DayHistoryCard from "../components/dayHistory/DayHistoryCard";
import TaskCard from "../components/taskCard/TaskCard";
import {useEffect, useState} from "react";
import apiCall from "../apiCall";
import DatePicker from "../components/DatePicker";
import {TabPanel} from "@material-ui/lab";


const Dashboard = () => {
  const [tab, setTab] = useState(0)

  return (
    <>
      <MenuBar tab={tab} setTab={setTab}/>
      <TabPanel value={tab} index={0}>
        qwe
      </TabPanel>
      <TabPanel value={tab} tabIndex={1}>
        asd
      </TabPanel>
      <TabPanel value={tab} tabIndex={2}>
        zxc
      </TabPanel>
    </>
  )
}

export default Dashboard
