
using System;

namespace Cars
{
	
	
	public class Person
	{
		public String name;
		
        //public int roleId { get { return roleId; } set { roleId = value; } }
        //public int OwnerId { get { return ownerId; } set { ownerId = value; } }
        //public String DocumentTitle { get { return _title; } set { _title = value; } }
        //public String FileName { get { return _filename; } set { _filename = value; } }
        //public String FileType { get { return _filetype; } set { _filetype = value; } }
        //public String ContentType { get { return _contentType; } set { _contentType = value; } }
        //public String DocumentType { get { return _docType; } set { _docType = value; } }
        //public int FileSize { get { return _size; } set { _size = value; } }
	    
		
		public Person(String name)
		{
			this.name = name;
		}
		
		public String getName() {
			for (int i = 0; i<10;i++);
			if (true) return this.name;
		}
		
		public String toString() {
			return this.name;
		}
	}
	
}