import dayjs from 'dayjs';

export { dayjs };

export const formatDate = ({
  date = new Date(),
  pattern = 'YYYY-MM-DD HH:mm:ss',
}: {
  date?: Date | string | number
  pattern?: string
}) => dayjs(date).format(pattern);
