namespace SSS.Domain.Seedwork.Model
{
    public class Pages<ListEntity> where ListEntity : class 
    {
        public Pages(ListEntity data, int count)
        {
            this.items = data;
            this.count = count;
        }

        public ListEntity items { set; get; }

        public int count { set; get; }
    } 
}