import {Box, CircularProgress, makeStyles, TextField} from "@material-ui/core";
import {Autocomplete, createFilterOptions} from "@material-ui/lab";
import {useEffect, useState} from "react";
import apiCall from "../../apiCall";
import {useQuery} from "react-query";
import {useDebounce} from "react-use";

const useStyles = makeStyles((theme) => ({
  fullWidth: {
    width: "100%"
  }
}))

const search = async ({queryKey}) => {
  const [, searchText] = queryKey
  const res = await apiCall("/api/tasks?query=" + searchText)
  return await res.json()
}

const TaskCreator = ({createTask, addTask}) => {

  const classes = useStyles()
  const filter = createFilterOptions();
  const [open, setOpen] = useState(false);
  const [searchText, setSearchText] = useState("")
  const [searchTextQuery, setSearchTextQuery] = useState("")

  useDebounce(() => {
      setSearchTextQuery(searchText)
  }, 500, [searchText])

  const query = useQuery(["searchTasks", searchTextQuery], search)
  console.log(query)
  const handleOnChange = (event, newValue) => {
    if (newValue && newValue.inputValue) {
      createTask(newValue.inputValue)
    } else if (newValue) {
      addTask(newValue)
    }
  }

  const handleFilterOptions = (options, params) => {
    const filtered = filter(options, params);
    if (params.inputValue !== "") {
      filtered.push({name: `Create "${params.inputValue}"`, inputValue: params.inputValue})
    }
    return filtered
  }

  return (
    <Box>
      {query.isLoading ? <CircularProgress color="inherit" size={20}/> : null}
      <Autocomplete
        renderInput={(params) => <TextField label={"Add task"} className={classes.fullWidth} {...params}/>}
        options={query.data?.items ?? []}
        filterOptions={handleFilterOptions}
        getOptionLabel={option => option.name}
        onChange={handleOnChange}
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => setOpen(false)}
        loading={query.isLoading}
        inputValue={searchText}
        onInputChange={(event, newValue) => setSearchText(newValue)}

      />
    </Box>
  )
}


export default TaskCreator
