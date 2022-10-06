import * as React from 'react';
import {Box, IconButton, Typography} from "@mui/joy";
import GitHubIcon from '@mui/icons-material/GitHub';

const styles = {
    header: {
        p: 2,
        gap: 2,
        bgcolor: 'white',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        gridColumn: '1 / -1',
        borderBottom: '1px solid',
        borderColor: 'divider',
        position: 'sticky',
        top: 0,
        zIndex: 1100,
    }
}

export default function Header() {
    return (
        <Box
            sx={styles.header}>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center'
                }}>
                <Typography component="h1" fontWeight="xl">
                    Test Data Service
                </Typography>
            </Box>
            <Box sx={{display: 'flex', flexDirection: 'row'}}>
                <IconButton
                    size="sm"
                    variant="outlined"
                    color="primary"
                    component="a"
                    href="https://github.com/auto1-oss/test-data-storage-service"
                >
                    <GitHubIcon/>
                </IconButton>
            </Box>
        </Box>
    );
}