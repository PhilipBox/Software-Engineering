#include <stdio.h>

void main()
{

	int variable = 0;		// solution : initialization.
	float flt = 3.4;		

	int i[10];
	i[9] = 1;				// solution : accesse allowed memory range.


	int a = 3;
	int* ptr = &a;			// solution : reference correct address.
	

	for (int i = 0; i < 5; i++)
	{
		variable += i;
	}

	variable = i[9] + *ptr;		// use variables

	printf("%f\n", flt);		// solution : use correct argument for float.
	
	printf("%d\n", variable / 4);	// solution : Don't divide by zero.
		// style error for never used variable)
		// -> solution : delete or use variables.
}

