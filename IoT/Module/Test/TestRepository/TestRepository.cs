using Common.Page;

using Furion.DependencyInjection;

namespace TestRepository
{
    public class TestRepository : ITransient
    {
        public Page<TestEntity.TestEntity> GetPage(int pageIndex, int pageSize)
        {
            var list = new List<TestEntity.TestEntity>();
            list.Add(new TestEntity.TestEntity() { Id = 1, Name = "a" });
            list.Add(new TestEntity.TestEntity() { Id = 2, Name = "b" });
            list.Add(new TestEntity.TestEntity() { Id = 3, Name = "c" });
            long total = 3;

            var result = new Page<TestEntity.TestEntity>
            {
                Rows = list,
                PageIndex = pageIndex,
                PageSize = pageSize,
                Count = total
            };
            return result;
        }
    }
}
