ContentProviderJoin
===================

This code gives a example about how to use Android ContentProviders / ContentResolvers / CursorLoaders / CursorAdapters

<b><u>What is the Content Resolver?</u></b>

The Content Resolver is the single, global instance in your application that provides access to your (and other applications') content providers. The Content Resolver behaves exactly as its name implies: it accepts requests from clients, and resolves these requests by directing them to the content provider with a distinct authority. To do this, the Content Resolver stores a mapping from authorities to Content Providers. This design is important, as it allows a simple and secure means of accessing other applications' Content Providers.

The Content Resolver includes the CRUD (create, read, update, delete) methods corresponding to the abstract methods (insert, delete, query, update) in the Content Provider class. The Content Resolver does not know the implementation of the Content Providers it is interacting with (nor does it need to know); each method is passed an URI that specifies the Content Provider to interact with.

<b><u>What is the Content Provider?</u></b>

Whereas the Content Resolver provides an abstraction from the application's Content Providers, Content Providers provides an abstraction from the underlying data source (i.e. a SQLite database). They provide mechanisms for defining data security (i.e. by enforcing read/write permissions) and offer a standard interface that connects data in one process with code running in another process.

Content Providers provide an interface for publishing and consuming data, based around a simple URI addressing model using the content:// schema. They enable you to decouble your application layers from the underlying data layers, making your application data-source agnostic by abstracting the underlying data source.

<b><u>Whats is in this project ?</u></b>

This project has 3 modules. SingleTable, TwoTables, JoinTables. Each project builds on top of another by adding few things to previous one.

<b><i>SingleTable</i></b>

<p>This projects introduces various concepts like</p> 
	1. SQLiteDatabase (Helper ?)
	2. Person Model / Table
	3. ContentProvider (Providing a interface to get data without knowing the undelying knowledge of storage)
	4. Activity (Displays the ListView and some buttons)
	5. ListView (Displaying the person from the database)
	6. CursorAdapter (The thing backing the ListView)
	7. CursorLoader (Async Loading of all objects from person table)

<b><i>TwoTables</i></b>

<p>This project adds on to previous project by</p>
	1. Edited - Added a new URI to ContentProvider
	2. Added - Department Model / Table
	3. Added - Activity (Displays ListView with some buttons)
	4. Added - ListView (Displays the departments from database)
	5. Added - CursorAdapter (The backing model for ListView)
	6. Added - CursorLoader (Async Loading of all objects from department table)
					
<b><i>JoinTables</i></b>

<p>This project adds on to previous project by</p>
	1. Edited - Added a new URI to ContentProvider
	2. Added - Helper Methods to help in join
	3. IMPORTANT:
	
	private void notifyUris(Uri itemUri, ContentObserver contentObserver) {
        	getContext().getContentResolver().notifyChange(itemUri, contentObserver);
        	getContext().getContentResolver().notifyChange(URI_PERSONS_DEPARTMENTS, contentObserver);
    	}

	4. Added - Activity (Displays ListView with some buttons)
        5. Added - ListView (Displays the join of person and departments from database)
        6. Added - CursorAdapter (The backing model for ListView)
        7. Added - CursorLoader (Async Loading of all objects from person and department table)

<p>	
<b><u>The Life of a Query</u></b>

So what exactly is the step-by-step process behind a simple query? As described above, when you query data from your database via the content provider, you don't communicate with the provider directly. Instead, you use the Content Resolver object to communicate with the provider. The specific sequence of events that occurs when a query is made is given below:

A call to getContentResolver().query(Uri, String, String, String, String) is made. The call invokes the Content Resolver's query method, not the ContentProvider's.
When the query method is invoked, the Content Resolver parses the uri argument and extracts its authority.
The Content Resolver directs the request to the content provider registered with the (unique) authority. As seen in the source code, this is done by calling the Content Provider's query method.
When the Content Provider's query method is invoked, the query is performed and a Cursor is returned (or an exception is thrown). The resulting behavior depends entirely on the Content Provider's implementation. 
