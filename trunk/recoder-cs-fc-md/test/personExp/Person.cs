
using System;

namespace Cars
{
	
	
	public class Person
	{
		public string name;
		
        public int roleId { get { return roleId; } set { roleId = value; } }
        //public int OwnerId { get { return ownerId; } set { ownerId = value; } }
        //public string DocumentTitle { get { return _title; } set { _title = value; } }
        //public string FileName { get { return _filename; } set { _filename = value; } }
        //public string FileType { get { return _filetype; } set { _filetype = value; } }
        //public string ContentType { get { return _contentType; } set { _contentType = value; } }
        //public string DocumentType { get { return _docType; } set { _docType = value; } }
        //public int FileSize { get { return _size; } set { _size = value; } }
	    
		
		public Person(string name)
		{
			this.name = name;
		}
		
		public string getName() {
			for (int i = 0; i<10;i++);
			if (true) return this.name;
		}
		
		protected string tostring() {
			return this.name;
		}
	}
	
}