/** \mainpage 201333186 Moon Ji-hwan


\section Doxygen Doxygen Exercise
- Exercise as C code



\section About About this C code

- Banker's Algorithm Simulator




\section advenced Used function

void Initial_AVAILABLE();
- read available resource from input file.

void create_PCB(int pid);
- read allocated information of process from input file.

void Safety_Algorithm();
- Safety algorithm which is to find safety sequence.

int AFTER_REQUEST_Safety_Algorithm();
- After request, Safety algorithm which is to find safety sequence.

int CHECK_RESOURCE(PCB *process);
- to check resources per process whether possible allocation or not.

void ALLOCATE_RESOURCE(PCB *process);
- After checking resource, if allocating is possible, allocate resources per process.
int CHECK_ALL();
- Compare remaining resources and required all resources.

void RESET();
- Reset information of processes, because restart after request allocating.

*/


/**
@file Bankers.c
@brief Banker's Algorithm Simulator
@brief Safety Algorithm of Deadlock
@author Jihwan Moon(git@PhilipBox)
@return none
@remark none
@version 1.0
*/


#include <stdio.h>
#include <stdlib.h>

#define FALSE 0
#define TRUE 1
#define MAX_RESOURCE 100
#define MAX_PROCESS 5

// DATA STRUCTURE
typedef struct pcb {
	int pid;
	int Allocation[MAX_RESOURCE];
	int MAX[MAX_RESOURCE];
	int need[MAX_RESOURCE];
	int work[MAX_RESOURCE];
	int FINISH;

	struct pcb *next;
}PCB;

PCB *head = NULL;
PCB *process = NULL;
PCB RQ;

int process_pid;
int Available[MAX_RESOURCE];
int Available2[MAX_RESOURCE];
int request[MAX_RESOURCE];
int total_resource;
int total_process;

FILE *fp, *wp;

void Initial_AVAILABLE();
void create_PCB(int pid);

void Safety_Algorithm();
int AFTER_REQUEST_Safety_Algorithm();

int CHECK_RESOURCE(PCB *process);
void ALLOCATE_RESOURCE(PCB *process);
int CHECK_ALL();
void RESET();

/**
@brief main(int argc, char *argv[]) 
@param inData - inData is a value will be pushed.
@return None
@details 
\li argc = 5;

\li argc[0] = directory name // command 
\li argv[1] = "input.txt"  // input file name 
\li argv[2] = "output.txt" // output file name 
\li argv[3] = "entered positive integer" // number of resources 
\li argv[4] = "entered positive integer" // number of processes 
*/
int main(int argc, char *argv[]) {
	//convert type from String to Integer
	total_resource = atoi(argv[3]);
	total_process = atoi(argv[4]);

	if (argc != 5)
	{
		printf("ERROR! the number of parameters. must be 3 parameters.\n");
	}//end if
	else
	{
		fp = fopen(argv[1], "r");
		wp = fopen(argv[2], "w");

		if (fp == NULL)
		{
			printf("ERROR! File read failed! \n");
			exit(0);
		}//end if [defensive code]

		 //To read input file's first line ( Initial resources information)
		Initial_AVAILABLE();

		//read allocated information of process from input file.
		for (int i = 0; i < total_process; i++)
		{
			//first, read the pid of process as parameter of create_PCB function
			fscanf(fp, "%d ", &process_pid);
			create_PCB(process_pid);
		}//end 1st for loop


		// read last line information of input file (request information)
		fscanf(fp, "%d ", &RQ.pid);
		for (int i = 0; i < total_resource; i++)
			fscanf(fp, "%d ", &RQ.Allocation[i]);
		 

		// print and save of result of program
		printf("%s\n", argv[1]);
		fprintf(wp, "%s\n", argv[1]);
		printf("current state : ");
		fprintf(wp, "current state : ");
		Safety_Algorithm();

		printf("After arriving request: ");
		fprintf(wp, "After arriving request: ");
		if (AFTER_REQUEST_Safety_Algorithm() == FALSE)
		{
			printf("unsafe!\n");
			fprintf(wp, "unsafe!\n");
		}
	}//end else
}//end main


/**	
@brief Safety_Algorithm function
@param none
@return none
@details Safety algorithm which is to find safety sequence.
*/
void Safety_Algorithm()
{
	//to check state of process (wheher allocated or not)
	int result_value[MAX_PROCESS] = { 0, };
	int result_index = 0;

	for (int i = 0; i <total_process; i++)
	{
		// function call : CHECK_ALL
		if ((result_index != total_process - 1) && CHECK_ALL())
		{
			result_value[result_index++] = -2013;			  // -2013 means my own number 
			break;									  //( to check remaining resources and required all resources.)
		} 

		PCB *temp = head;
		while (temp)
		{
			if (temp->FINISH == FALSE) //if process need to allocate resource
			{
				if (CHECK_RESOURCE(temp) == TRUE)			  // need to allocate resource && possible allocating
				{
					ALLOCATE_RESOURCE(temp);				  // function call : allocate resource
					temp->FINISH = TRUE;					  //  change state of process ( TURE : allocating finish )
					result_value[result_index++] = temp->pid; // save process pid 
					break;
				}
			}
			temp = temp->next;	// change next process
		}//end while loop
	}//end for loop ( times of process numbers )

	if (result_value[result_index - 1] != -2013 && result_index != total_process)	// to check unsafe
	{
		printf("unsafe!");
		fprintf(wp, "unsafe!");
	}
	else																			// if not unsafe
	{
		for (int i = 0; i != result_index; i++)
		{
			if (result_value[i] == -2013)		// remaining resources exsist more than  required all resources.
			{
				printf("->ALL");
				fprintf(wp, "->ALL");
				break;
			}
			if (i != 0 && i != result_index)	// print ->
			{
				printf("->");
				fprintf(wp, "->");
			}									// print pid of process
			printf("%d", result_value[i]);
			fprintf(wp, "%d", result_value[i]);
		}
	}
	printf("\n");
	fprintf(wp, "\n");
}


/**
@brief AFTER_REQUEST_Safety_Algorithm function 
@param : none
@return If allocating is impossible then return FALSE\n
				otherwise, progress the function\n
	After request, Safety algorithm which is to find safety sequence.\n
*/ 
int AFTER_REQUEST_Safety_Algorithm()
{
	// function call : Reset information of processes.
	RESET();

	// reupdating of Available resources.
	for (int i = 0; i < total_resource; i++)
	{
		Available[i] = Available2[i];
	}

	
	PCB *temp = head;
	//RQ menas request
	int request_pid = RQ.pid;

	for (int i = 0; i < total_resource; i++)
	{
		if (RQ.Allocation[i] > Available[i])		// if allocating is not possible
			return FALSE;							// return FALSE ( unsafe )
	}//end for loop (to check resource per processes)

	while (temp)
	{
		if (temp->pid == request_pid)
		{
			for (int j = 0; j < total_resource; j++)
			{
				temp->Allocation[j] += RQ.Allocation[j];
			}//end for loop
		}
		temp = temp->next;
	}

	//Above procedure is check request and return result.
	//if allocating is possible, bottom procedure is progressed

	// bottom procedure is same as Safety_Algorithm function.

	//to check state of process (wheher allocated or not)
	int result_value[MAX_PROCESS] = { 0, };
	int result_index = 0;

	for (int i = 0; i <total_process; i++)
	{
		// function call : CHECK_ALL
		if ((result_index != total_process - 1) && CHECK_ALL())
		{
			result_value[result_index++] = -2013;			  // -2013 means my own number ( to check remaining resources and required all resources.)
			break;
		}

		PCB *temp = head;
		while (temp)
		{
			if (temp->FINISH == FALSE) //if process need to allocate resource
			{
				if (CHECK_RESOURCE(temp) == TRUE)			  // need to allocate resource && possible allocating
				{
					ALLOCATE_RESOURCE(temp);				  // function call : allocate resource
					temp->FINISH = TRUE;					  //  change state of process ( TURE : allocating finish )
					result_value[result_index++] = temp->pid; // save process pid 
					break;
				}
			}
			temp = temp->next;	// change next process
		}//end while loop
	}//end for loop ( times of process numbers )

	if (result_value[result_index - 1] != -2013 && result_index != total_process)	// to check unsafe
	{
		printf("unsafe!");
		fprintf(wp, "unsafe!");
	}
	else																			// if not unsafe
	{
		for (int i = 0; i != result_index; i++)
		{
			if (result_value[i] == -2013)		// remaining resources exsist more than  required all resources.
			{
				printf("->ALL");
				fprintf(wp, "->ALL");
				break;
			}
			if (i != 0 && i != result_index)	// print ->
			{
				printf("->");
				fprintf(wp, "->");
			}									// print pid of process
			printf("%d", result_value[i]);
			fprintf(wp, "%d", result_value[i]);
		}
	}
	printf("\n");
	fprintf(wp, "\n");
}

/**
@brief CHECK_ALL function
@param none
@return FALSE or TURE 
@details Compare remaining resources and required all resources.
*/
int CHECK_ALL()
{
	PCB *temp = head;
	int all_state = TRUE;						// if TRUE, remaining resources can allocate all resources required.
	while (temp)			
	{
		if (temp->FINISH == TRUE)				// if process finished allocating, then continue.
		{
			temp = temp->next;
			continue;
		}
		else									// if the process requires allocation
		{
			if (CHECK_RESOURCE(temp) == FALSE)	// check whether allocating possible or not
			{
				all_state = FALSE;				// if impossible, return FALSE
				break;
			}
			temp = temp->next;
		}
	}
	return all_state;							// if possible, then return TRUE
}


/**
@brief CHECK_RESOURCE function
@param PCB *process (structure of process)
@return FALSE or TURE
@details to check resources per process whether possible allocation or not.
*/
int CHECK_RESOURCE(PCB *process)
{
	int state = TRUE;
	for (int i = 0; i < total_resource; i++)
	{
		if (process->need[i] > Available[i])
			state = FALSE;
	}//end for loop ( to check available resourcces )
	return state;
}


/**
@brief ALLOCATE_RESOURCE function
@param PCB *process (structure of process )
@return none
@details After checking resource, if allocating is possible then allocate resources per process.
*/
void ALLOCATE_RESOURCE(PCB *process)
{
	for (int i = 0; i< total_resource; i++)
	{
		Available[i] += process->work[i];
	}
}


 /**
@brief create_PCB function
@param int pid (pid of process)
@return none
@details read allocated information of process from input file.
 */
void create_PCB(int pid)
{
	process = (PCB*)malloc(sizeof(PCB));
	process->pid = pid;

	// for loop (to read allocated resource)
	for (int j = 0; j < total_resource; j++)
	{
		fscanf(fp, "%d ", &process->Allocation[j]);
		process->work[j] = process->Allocation[j];
	}
	// for loop (to read MAX resource)
	for (int j = 0; j < total_resource; j++)
		fscanf(fp, "%d ", &process->MAX[j]);

	// for loop (to calculate resource needed)
	for (int j = 0; j < total_resource; j++)
		process->need[j] = process->MAX[j] - process->Allocation[j];
	
	// initialize FINISH state to FASLE
	process->FINISH = FALSE;

	process->next = NULL;
	if (head == NULL)
		head = process;
	else
	{
		PCB *temp = head;
		while (temp->next)
			temp = temp->next;
		temp->next = process;
	}
}


/**
@brief Initial_AVAILABLE function
@param none
@return none
@details read available resource from input file.
*/
void Initial_AVAILABLE()
{
	for (int i = 0; i<total_resource; i++)
	{
		fscanf(fp, "%d ", &Available[i]);
		Available2[i] = Available[i];
	}
}


/**
@brief RESET function
@paramnone
@return none
@details Reset information of processes, because restart after request allocating.
*/	
void RESET()
{
	PCB*temp = head;

	while (temp)
	{
		temp->FINISH = FALSE;
		for (int i = 0; i < total_resource; i++)
		{//reset work variable.
			temp->work[i] = temp->Allocation[i];
		}
		temp = temp->next;
	}
}