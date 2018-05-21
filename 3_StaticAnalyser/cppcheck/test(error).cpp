#include <stdio.h>

void main()
{

	int variable;	//error

	int i[10];
	i[10] = 0;		// error 

	int a = 3;
	int* ptr = NULL;
	*ptr = 200;		// error 

	for (int i = 0; i < 5; i++)
	{
		variable += i;
	}


	printf("%d\n", variable / 1);	// error 
									// error - never used 'i' 

}