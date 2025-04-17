--
-- This file is part of the auto1-oss/test-data-storage-service.
--
-- (c) AUTO1 Group SE https://www.auto1-group.com
--
-- For the full copyright and license information, please view the LICENSE
-- file that was distributed with this source code.
--

CREATE INDEX IF NOT EXISTS idx_omni_queue_omni_type_id_archived_id
    ON test_data_storage.omni_queue (omni_type_id, archived, id);
