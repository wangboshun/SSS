using System;

namespace SSS.Domain.Seedwork.Model
{
    public abstract class Entity
    {
        /// <summary>
        ///     主键Id
        /// </summary> 
        public string Id { get; set; }

        /// <summary>
        ///     创建时间
        /// </summary>
        public DateTime CreateTime { set; get; }

        /// <summary>
        ///     是否删除 0代表正常 1代表已删除
        /// </summary>
        public int IsDelete { set; get; }

        public override bool Equals(object obj)
        {
            var compareTo = obj as Entity;

            if (ReferenceEquals(this, compareTo)) return true;
            if (ReferenceEquals(null, compareTo)) return false;

            return Id.Equals(compareTo.Id);
        }

        public static bool operator ==(Entity a, Entity b)

        {
            if (ReferenceEquals(a, null) && ReferenceEquals(b, null))
                return true;

            if (ReferenceEquals(a, null) || ReferenceEquals(b, null))
                return false;

            return a.Equals(b);
        }

        public static bool operator !=(Entity a, Entity b)
        {
            return !(a == b);
        }

        public override int GetHashCode()
        {
            return GetType().GetHashCode() * 907 + Id.GetHashCode();
        }

        public override string ToString()
        {
            return GetType().Name + " [Id=" + Id + "]";
        }
    }
}