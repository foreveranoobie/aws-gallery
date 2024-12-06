CREATE TABLE public."user" (
	"id" serial4 NOT NULL,
	"name" varchar NOT NULL,
	"password" varchar NOT NULL,
	"role" int4 NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id, name),
	CONSTRAINT user_user_role_fk FOREIGN KEY ("role") REFERENCES public.user_role(id)
);