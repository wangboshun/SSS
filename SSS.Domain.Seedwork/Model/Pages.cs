namespace SSS.Domain.Seedwork.Model
{
    public class Pages<ListEntity> where ListEntity : class
    {
        public Pages(ListEntity data, int count)
        {
            items = data;
            this.count = count;
        }

        public int count { set; get; }
        public ListEntity items { set; get; }
    }
}