import * as React from 'react';
import Typography from '@mui/joy/Typography';
import {Box, Chip, IconButton} from "@mui/joy";
import DownloadIcon from '@mui/icons-material/Download';
import Sheet from "@mui/joy/Sheet";

export default function DataQueueCard({queue, fetchItem}) {


    const handleClickFetchItem = () => {
        fetchItem(queue.dataType)
    }

    return (
        <Sheet row variant="outlined"  sx={(theme) => ({
            borderRadius: "8px",
            p: 2,
            gap: 'clamp(0px, (100% - 360px + 32px) * 999, 16px)',
            transition: 'transform 0.3s, border 0.3s',
            boxShadow: 'none',
            '&:hover': {
                borderColor: theme.vars.palette.primary.outlinedHoverBorder,
                transform: 'translateY(-2px)',
            }
        })}>
            <Box display={"flex"} justifyContent={"space-between"}>
                <Box sx={{display: 'flex', alignItems: 'center'}}>
                    <Typography fontWeight="md" textColor="neutral.700" >
                        {queue.dataType}
                    </Typography>
                </Box>
                <Box sx={{gap: 1.5, display: 'flex', justifyContent: 'flex-end'}}>
                    <Chip variant={"soft"} sx={{borderRadius: '8px'}}>{queue.itemCount}</Chip>
                    <IconButton
                        onClick={handleClickFetchItem}
                        size="sm"
                        variant="outlined"
                        color="primary"
                    >
                        <DownloadIcon/>
                    </IconButton>
                </Box>
            </Box>
        </Sheet>
    );
}
