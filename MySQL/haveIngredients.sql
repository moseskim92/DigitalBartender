
DELIMITER //

CREATE FUNCTION _have_ingredients
(
	temp_cocktail_id int
)
RETURNS INT

BEGIN

DECLARE	liquid_id	int;
DECLARE	number		int;
DECLARE	stocked	bit;
DECLARE t1 int;

	SET stocked = 1;
	SET number = 0;

	SET number = (SELECT COUNT(*) FROM _mix WHERE _cocktail_id = temp_cocktail_id AND _mix._ingredient_id NOT IN (SELECT _liquid FROM _inventory));

	IF number > 0
	THEN
		SET stocked = 0;
	END IF;

	RETURN stocked;
	
END;  

//


DELIMITER ;