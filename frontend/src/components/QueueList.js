import DataQueueCard from "./DataQueueCard";
import React, {memo} from 'react'
import {Box} from "@mui/joy";

const styles = {
    cardsContainer: {
        borderRadius: 'sm',
        display: 'grid',
        gap: 2,
        bgcolor: 'background.componentBg'
    }
}


const QueueList = ({list, fetchData}) => {

    const handleClickFetchItem = (dataItem) => {
        fetchData(dataItem)
    }

    return (
        <Box sx={styles.cardsContainer}>
            {list.map((queue) => (
                <DataQueueCard key={queue.dataType} queue={queue} fetchItem={handleClickFetchItem}/>
            ))}
        </Box>
    )
}

export default memo(QueueList);