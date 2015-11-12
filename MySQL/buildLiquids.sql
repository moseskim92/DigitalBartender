DROP TABLE IF EXISTS _liquids;
DROP PROCEDURE IF EXISTS _build_ing_table;

CREATE TABLE _liquids
(
	_id int NOT NULL,
	_name varchar(255) NULL
);


DELIMITER //

CREATE PROCEDURE _build_ing_table ()


BEGIN
	DECLARE	iLiquidID INT DEFAULT 1;
	DECLARE	vcIngName VARCHAR(255);	
	DECLARE done INT DEFAULT FALSE;
	DECLARE ingredientList CURSOR FOR SELECT DISTINCT _ingredient_id FROM _mix;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	OPEN ingredientList; 

	
myloop: LOOP

	FETCH ingredientList INTO vcIngName;
	IF NOT EXISTS(SELECT iLiquidID = _id FROM _liquids WHERE _name = vcIngName) 
	THEN
			SET iLiquidID = iLiquidID + 1;
		
			INSERT INTO _liquids(_id, _name) VALUES (iLiquidID, vcIngName);
	END IF;

		UPDATE _mix SET _ingredient_id = iLiquidID WHERE _ingredient_id = vcIngName;			
	

IF done THEN
LEAVE myloop;
END IF;

END LOOP;

CLOSE ingredientList;

END//

DELIMITER ;