
using Common.Page;

using Furion.DependencyInjection;
using TestEntity.Dto;
namespace TestApplication
{
    public class TestService : ITransient
    {
        private readonly TestRepository.TestRepository _testRepository;

        public TestService(TestRepository.TestRepository testRepository)
        {
            _testRepository = testRepository;
        }

        public Page<TestEntity.TestEntity> GetPage(TestQueryPageDto dto)
        {
            return _testRepository.GetPage(dto.PageIndex, dto.PageSize);
        }
    }
}
