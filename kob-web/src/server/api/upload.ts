import type { ApiResponse } from '~/types';
import api from '~/utils/axios';

export const UploadApi = {
  uploadFile: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    return api.post<ApiResponse<string>>('/api/upload/file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
};
